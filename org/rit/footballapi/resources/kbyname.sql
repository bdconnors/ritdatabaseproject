SELECT  player.playerid,gplayed,jerseynumber,pos,player.firstname,player.lastname,team,fgAtt,fgmd,xpatt,xpmd
 FROM player
 INNER JOIN playercumkick ON player.playerid = playercumkick.playerid
 INNER JOIN playercummisc ON player.playerid = playercummisc.playerid
 WHERE player.firstname = ? AND player.lastname = ?;