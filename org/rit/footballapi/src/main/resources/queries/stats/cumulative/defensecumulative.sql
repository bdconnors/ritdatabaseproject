SELECT defensecumstats.team,pa,sck,intc,fum,sfty,inttd,fumtd,krtd,prtd,kblk,xpblk
 FROM defensecumstats
 INNER JOIN miscdefensecumstats ON defensecumstats.team = miscdefensecumstats.team
 WHERE defensecumstats.team = ?;